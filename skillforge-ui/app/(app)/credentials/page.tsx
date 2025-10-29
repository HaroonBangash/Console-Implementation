"use client";

import * as React from "react";
import { useToast } from "@/components/ui/use-toast";
import { PageHeader } from "@/components/page-header";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { CopyField } from "@/components/copy-field";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { certificates } from "@/mock-api/certificates";

export default function CredentialWalletPage() {
  const { addToast } = useToast();
  const [shareLinkOpen, setShareLinkOpen] = React.useState(false);

  return (
    <div className="space-y-6">
      <PageHeader
        title="Credential wallet"
        description="Manage verifiable credentials issued on SkillForge's trust network."
      />
      <Card>
        <CardHeader>
          <CardTitle className="text-xl">Issued credentials</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Program</TableHead>
                <TableHead>Issuer</TableHead>
                <TableHead>Issued</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Verify URL</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {certificates.map((certificate) => (
                <TableRow key={certificate.id}>
                  <TableCell className="font-medium">{certificate.program}</TableCell>
                  <TableCell>{certificate.issuer}</TableCell>
                  <TableCell>{new Date(certificate.issueDate).toLocaleDateString()}</TableCell>
                  <TableCell>
                    <Badge variant="success">{certificate.status}</Badge>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        navigator.clipboard.writeText(certificate.verifyUrl);
                        addToast({ title: "Copied", description: "Verify URL copied to clipboard." });
                      }}
                    >
                      Copy URL
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle className="text-xl">Share credential</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <p className="text-sm text-muted-foreground">
            Generate a public link to share your credential wallet with employers.
          </p>
          <CopyField value="https://skillforge.example/verify?hash=abc123" label="Public share link" />
          <Button
            variant="secondary"
            onClick={() => {
              setShareLinkOpen(true);
              addToast({ title: "Share link ready", description: "Send to hiring partners for instant verification." });
            }}
          >
            Share
          </Button>
          {shareLinkOpen ? (
            <div className="rounded-2xl border border-dashed border-border p-4 text-sm text-muted-foreground">
              <p className="font-medium">Share preview</p>
              <p>Your public wallet link includes name, programme, issuer, and verification status.</p>
            </div>
          ) : null}
        </CardContent>
      </Card>
    </div>
  );
}
